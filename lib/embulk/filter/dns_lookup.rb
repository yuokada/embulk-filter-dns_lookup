Embulk::JavaPlugin.register_filter(
  "dns_lookup", "org.embulk.filter.dns_lookup.DnsLookupFilterPlugin",
  File.expand_path('../../../../classpath', __FILE__))
